package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.velib.api.VelibStationController;
import fr.esgi.velib.api.VelibStationController.StationStats;
import fr.esgi.velib.dto.request.VelibStationRequestDto;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import fr.esgi.velib.service.VelibStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static samples.VelibStationDtoSample.*;

@ExtendWith(MockitoExtension.class)
class VelibStationControllerTest {

    @Mock
    private VelibStationService velibStationService;

    @InjectMocks
    private VelibStationController velibStationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(velibStationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnHealthStatus() throws Exception {
        StationStats stats = new StationStats(150L, 12L, 120L, 2500L, 800L);
        when(velibStationService.getStationStats()).thenReturn(stats);

        mockMvc.perform(get("/stations/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Station Velib API is running with JSON data! 150 stations loaded."));

        verify(velibStationService).getStationStats();
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetVelibStationsByTowns() throws Exception {
        VelibStationRequestDto request = parisLyonRequest();
        VelibStationResponseDto expectedResponse = allStationsResponse();

        when(velibStationService.getStationsByTowns(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(post("/stations/velibs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(4))
                .andExpect(jsonPath("$.stations[0].name").value("République"))
                .andExpect(jsonPath("$.stations[0].nomCommune").value("Paris"));

        verify(velibStationService).getStationsByTowns(Arrays.asList("Paris", "Lyon"));
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetAllStations() throws Exception {
        VelibStationResponseDto expectedResponse = allStationsResponse();
        when(velibStationService.getAllStations()).thenReturn(expectedResponse);

        mockMvc.perform(get("/stations/velibs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(4));

        verify(velibStationService).getAllStations();
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetStationsByTown() throws Exception {
        String town = "Paris";
        VelibStationResponseDto expectedResponse = parisStationsResponse();
        when(velibStationService.getStationsByTown(town)).thenReturn(expectedResponse);

        mockMvc.perform(get("/stations/velibs/{town}", town))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(2))
                .andExpect(jsonPath("$.stations[0].nomCommune").value("Paris"));

        verify(velibStationService).getStationsByTown(town);
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetAvailableStationsByTowns() throws Exception {
        VelibStationRequestDto request = parisLyonRequest();
        VelibStationResponseDto expectedResponse = availableStationsResponse();

        when(velibStationService.getAvailableStationsByTowns(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(post("/stations/velibs/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(3))
                .andExpect(jsonPath("$.stations[0].bikesAvailable").value(20))
                .andExpect(jsonPath("$.stations[0].isRenting").value("OUI"));

        verify(velibStationService).getAvailableStationsByTowns(Arrays.asList("Paris", "Lyon"));
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetAvailableCommunes() throws Exception {
        List<String> expectedCommunes = Arrays.asList("Lyon", "Marseille", "Paris");
        when(velibStationService.getAvailableCommunes()).thenReturn(expectedCommunes);

        mockMvc.perform(get("/stations/communes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("Lyon"))
                .andExpect(jsonPath("$[1]").value("Marseille"))
                .andExpect(jsonPath("$[2]").value("Paris"));

        verify(velibStationService).getAvailableCommunes();
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldGetStationStats() throws Exception {
        StationStats expectedStats = new StationStats(150L, 12L, 120L, 2500L, 800L);
        when(velibStationService.getStationStats()).thenReturn(expectedStats);

        mockMvc.perform(get("/stations/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalStations").value(150))
                .andExpect(jsonPath("$.totalCommunes").value(12))
                .andExpect(jsonPath("$.availableStations").value(120))
                .andExpect(jsonPath("$.totalBikes").value(2500))
                .andExpect(jsonPath("$.totalDocks").value(800));

        verify(velibStationService).getStationStats();
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldHandleEmptyRequest() throws Exception {
        VelibStationRequestDto emptyRequest = emptyRequest();
        VelibStationResponseDto emptyResponse = emptyResponse();

        when(velibStationService.getStationsByTowns(anyList())).thenReturn(emptyResponse);

        mockMvc.perform(post("/stations/velibs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(0));

        verify(velibStationService).getStationsByTowns(Arrays.asList());
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldHandleUnknownTown() throws Exception {
        String unknownTown = "UnknownCity";
        VelibStationResponseDto emptyResponse = emptyResponse();
        when(velibStationService.getStationsByTown(unknownTown)).thenReturn(emptyResponse);

        mockMvc.perform(get("/stations/velibs/{town}", unknownTown))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stations").isArray())
                .andExpect(jsonPath("$.stations.length()").value(0));

        verify(velibStationService).getStationsByTown(unknownTown);
        verifyNoMoreInteractions(velibStationService);
    }

    @Test
    void shouldTestDirectMethodCall() {
        StationStats expectedStats = new StationStats(150L, 12L, 120L, 2500L, 800L);
        when(velibStationService.getStationStats()).thenReturn(expectedStats);

        String result = velibStationController.test();

        assertEquals("Station Velib API is running with JSON data! 150 stations loaded.", result);
        verify(velibStationService).getStationStats();
    }
}
