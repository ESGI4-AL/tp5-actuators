package fr.esgi.rent.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.request.RentalPropertyFilterDto;
import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.mapper.RentalPropertyMapper;
import fr.esgi.rent.repository.RentalPropertyRepository;
import fr.esgi.rent.service.RentalPropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static fr.esgi.rent.samples.RentalPropertyDtoSample.*;
import static fr.esgi.rent.samples.RentalPropertyEntitySample.*;
import static fr.esgi.rent.samples.RentalPropertyFilterDtoSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RentalPropertyResourceTest {

    @Mock
    private RentalPropertyRepository rentalPropertyRepository;

    @Mock
    private RentalPropertyMapper rentalPropertyMapper;

    @Mock
    private RentalPropertyService rentalPropertyService;

    @InjectMocks
    private RentalPropertyResource rentalPropertyResource;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rentalPropertyResource).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllRentalProperties() throws Exception {
        List<RentalPropertyEntity> entities = Arrays.asList(rentalPropertyEntity());
        List<RentalPropertyResponseDto> expectedDtos = rentalPropertyResponseDtoList();

        when(rentalPropertyService.findRentalProperties(null)).thenReturn(entities);
        when(rentalPropertyMapper.mapToDtoList(entities)).thenReturn(expectedDtos);

        mockMvc.perform(get("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Beautiful apartment in city center"))
                .andExpect(jsonPath("$[0].town").value("Paris"))
                .andExpect(jsonPath("$[0].rentAmount").value(1200.0));

        verify(rentalPropertyService).findRentalProperties(null);
        verify(rentalPropertyMapper).mapToDtoList(entities);
        verifyNoMoreInteractions(rentalPropertyService, rentalPropertyMapper);
    }

    @Test
    void shouldGetRentalPropertyById() throws Exception {
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        RentalPropertyEntity entity = rentalPropertyEntity();
        RentalPropertyResponseDto responseDto = rentalPropertyResponseDto();

        when(rentalPropertyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(rentalPropertyMapper.mapToDto(entity)).thenReturn(responseDto);

        mockMvc.perform(get("/api/rental-properties/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Beautiful apartment in city center"))
                .andExpect(jsonPath("$.town").value("Paris"))
                .andExpect(jsonPath("$.rentAmount").value(1200.0));

        verify(rentalPropertyRepository).findById(id);
        verify(rentalPropertyMapper).mapToDto(entity);
        verifyNoMoreInteractions(rentalPropertyRepository, rentalPropertyMapper);
    }

    @Test
    void givenUnknownPropertyId_shouldReturn404() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(rentalPropertyRepository.findById(unknownId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rental-properties/{id}", unknownId))
                .andExpect(status().isNotFound());

        verify(rentalPropertyRepository).findById(unknownId);
        verifyNoMoreInteractions(rentalPropertyRepository);
        verifyNoInteractions(rentalPropertyMapper);
    }

    @Test
    void shouldCreateRentalProperty() throws Exception {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        RentalPropertyEntity entity = rentalPropertyEntity();
        RentalPropertyResponseDto responseDto = rentalPropertyResponseDto();

        when(rentalPropertyMapper.mapToEntity(any(RentalPropertyRequestDto.class))).thenReturn(entity);
        when(rentalPropertyRepository.save(entity)).thenReturn(entity);
        when(rentalPropertyMapper.mapToDto(entity)).thenReturn(responseDto);

        mockMvc.perform(post("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Beautiful apartment in city center"))
                .andExpect(jsonPath("$.town").value("Paris"));

        verify(rentalPropertyMapper).mapToEntity(any(RentalPropertyRequestDto.class));
        verify(rentalPropertyRepository).save(entity);
        verify(rentalPropertyMapper).mapToDto(entity);
        verifyNoMoreInteractions(rentalPropertyMapper, rentalPropertyRepository);
    }

    @Test
    void givenInvalidRentalPropertyData_shouldReturn400() throws Exception {
        RentalPropertyRequestDto invalidDto = invalidRequestDto();

        mockMvc.perform(post("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(rentalPropertyService, rentalPropertyMapper, rentalPropertyRepository);
    }

    @Test
    void givenInvalidPropertyType_shouldReturn400() throws Exception {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        when(rentalPropertyMapper.mapToEntity(any(RentalPropertyRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Property type not found: UnknownType"));

        mockMvc.perform(post("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(rentalPropertyMapper).mapToEntity(any(RentalPropertyRequestDto.class));
        verifyNoMoreInteractions(rentalPropertyMapper);
        verifyNoInteractions(rentalPropertyRepository);
    }

    @Test
    void getRentalProperties_WithFilter_ShouldCallServiceWithFilter() throws Exception {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> entities = Arrays.asList(rentalPropertyEntity());
        List<RentalPropertyResponseDto> expectedDtos = Arrays.asList(rentalPropertyResponseDto());

        when(rentalPropertyService.findRentalProperties(any(RentalPropertyFilterDto.class))).thenReturn(entities);
        when(rentalPropertyMapper.mapToDtoList(entities)).thenReturn(expectedDtos);

        mockMvc.perform(get("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(rentalPropertyService).findRentalProperties(any(RentalPropertyFilterDto.class));
        verify(rentalPropertyMapper).mapToDtoList(entities);
        verifyNoMoreInteractions(rentalPropertyService, rentalPropertyMapper);
    }

    @Test
    void getRentalProperties_WithoutFilter_ShouldReturnAllProperties() throws Exception {
        List<RentalPropertyEntity> entities = rentalPropertyList();
        List<RentalPropertyResponseDto> expectedDtos = rentalPropertyResponseDtoList();

        when(rentalPropertyService.findRentalProperties(null)).thenReturn(entities);
        when(rentalPropertyMapper.mapToDtoList(entities)).thenReturn(expectedDtos);

        mockMvc.perform(get("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(rentalPropertyService).findRentalProperties(null);
        verify(rentalPropertyMapper).mapToDtoList(entities);
        verifyNoMoreInteractions(rentalPropertyService, rentalPropertyMapper);
    }

    @Test
    void getRentalProperty_DirectCall_WithExistingId_ShouldReturnOk() {
        UUID id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        RentalPropertyEntity entity = rentalPropertyEntity();
        RentalPropertyResponseDto responseDto = rentalPropertyResponseDto();

        when(rentalPropertyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(rentalPropertyMapper.mapToDto(entity)).thenReturn(responseDto);

        var response = rentalPropertyResource.getRentalProperty(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void getRentalProperty_DirectCall_WithNonExistingId_ShouldReturnNotFound() {
        UUID id = UUID.randomUUID();
        when(rentalPropertyRepository.findById(id)).thenReturn(Optional.empty());

        var response = rentalPropertyResource.getRentalProperty(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createRentalProperty_DirectCall_WithValidData_ShouldReturnCreated() {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        RentalPropertyEntity entity = rentalPropertyEntity();
        RentalPropertyResponseDto responseDto = rentalPropertyResponseDto();

        when(rentalPropertyMapper.mapToEntity(requestDto)).thenReturn(entity);
        when(rentalPropertyRepository.save(entity)).thenReturn(entity);
        when(rentalPropertyMapper.mapToDto(entity)).thenReturn(responseDto);

        var response = rentalPropertyResource.createRentalProperty(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void createRentalProperty_DirectCall_WithInvalidData_ShouldReturnBadRequest() {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        when(rentalPropertyMapper.mapToEntity(requestDto))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        var response = rentalPropertyResource.createRentalProperty(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createRentalProperty_WithStudioType_ShouldReturnCreated() throws Exception {
        RentalPropertyRequestDto studioRequest = studioRequestDto();
        RentalPropertyEntity studioEntity = studioEntity();
        RentalPropertyResponseDto studioResponse = studioResponseDto();

        when(rentalPropertyMapper.mapToEntity(any(RentalPropertyRequestDto.class))).thenReturn(studioEntity);
        when(rentalPropertyRepository.save(studioEntity)).thenReturn(studioEntity);
        when(rentalPropertyMapper.mapToDto(studioEntity)).thenReturn(studioResponse);

        mockMvc.perform(post("/api/rental-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studioRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Cozy studio in downtown"))
                .andExpect(jsonPath("$.town").value("Lyon"))
                .andExpect(jsonPath("$.rentAmount").value(800.0));

        verify(rentalPropertyMapper).mapToEntity(any(RentalPropertyRequestDto.class));
        verify(rentalPropertyRepository).save(studioEntity);
        verify(rentalPropertyMapper).mapToDto(studioEntity);
    }
}
