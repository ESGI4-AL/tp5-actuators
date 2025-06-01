package repository;

import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.repository.VelibStationRepository;
import fr.esgi.velib.service.DataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samples.VelibStationEntitySample;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VelibStationRepositoryTest {

    @Mock
    private DataService dataService;

    @InjectMocks
    private VelibStationRepository velibStationRepository;

    @Test
    void findAll_ShouldReturnAllStations() {
        List<VelibStationEntity> expectedStations = VelibStationEntitySample.allStations();
        when(dataService.getAllStations()).thenReturn(expectedStations);

        List<VelibStationEntity> result = velibStationRepository.findAll();

        assertThat(result)
                .isNotNull()
                .hasSize(5)
                .containsExactlyElementsOf(expectedStations);

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoStations() {
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.emptyList());

        List<VelibStationEntity> result = velibStationRepository.findAll();

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommuneIn_ShouldReturnStationsFromSpecifiedCommunes() {
        List<String> communes = Arrays.asList("Paris", "Lyon");
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommuneIn(communes);

        assertThat(result)
                .isNotNull()
                .hasSize(4)
                .extracting(VelibStationEntity::getNomCommune)
                .containsOnly("Paris", "Lyon");

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommuneIn_ShouldReturnPartialList_WhenSomeCommunesMatch() {
        List<String> communes = Arrays.asList("Paris");
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommuneIn(communes);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(VelibStationEntity::getNomCommune)
                .containsOnly("Paris");

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommuneIn_ShouldReturnEmptyList_WhenNoCommuneMatches() {
        List<String> communes = Arrays.asList("Toulouse", "Bordeaux");
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommuneIn(communes);

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommuneIn_ShouldReturnEmptyList_WhenCommunesListIsEmpty() {
        List<String> communes = Collections.emptyList();
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommuneIn(communes);

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommune_ShouldReturnStationsFromSpecifiedCommune() {
        String commune = "Paris";
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommune(commune);

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(VelibStationEntity::getNomCommune)
                .containsOnly("Paris");

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommune_ShouldReturnEmptyList_WhenCommuneDoesNotExist() {
        String commune = "Toulouse";
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        List<VelibStationEntity> result = velibStationRepository.findByNomCommune(commune);

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommune_ShouldThrowNullPointerException_WhenCommuneIsNull() {
        String commune = null;
        when(dataService.getAllStations()).thenReturn(VelibStationEntitySample.allStations());

        assertThatThrownBy(() -> velibStationRepository.findByNomCommune(commune))
                .isInstanceOf(NullPointerException.class);

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findByNomCommune_ShouldHandleStationsWithNullNomCommune() {
        VelibStationEntity stationWithNullCommune = new VelibStationEntity(
                "NULL001", "Station Null", "OUI", 10, 5, 5, 3, 2,
                "OUI", "OUI", "2024-12-31T23:59:59+00:00",
                2.0, 48.0, null, "00000", null
        );

        List<VelibStationEntity> stationsWithNull = Arrays.asList(
                VelibStationEntitySample.parisStation(),
                stationWithNullCommune
        );

        when(dataService.getAllStations()).thenReturn(stationsWithNull);

        List<VelibStationEntity> result = velibStationRepository.findByNomCommune("Paris");

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .extracting(VelibStationEntity::getNomCommune)
                .containsOnly("Paris");

        verify(dataService, times(1)).getAllStations();
    }

    @Test
    void findAllCommunes_ShouldReturnAllAvailableCommunes() {
        List<String> expectedCommunes = Arrays.asList("Lyon", "Marseille", "Paris");
        when(dataService.getAvailableCommunes()).thenReturn(expectedCommunes);

        List<String> result = velibStationRepository.findAllCommunes();

        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly("Lyon", "Marseille", "Paris");

        verify(dataService, times(1)).getAvailableCommunes();
    }

    @Test
    void findAllCommunes_ShouldReturnEmptyList_WhenNoCommunesAvailable() {
        when(dataService.getAvailableCommunes()).thenReturn(Collections.emptyList());

        List<String> result = velibStationRepository.findAllCommunes();

        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(dataService, times(1)).getAvailableCommunes();
    }

    @Test
    void constructor_ShouldInitializeDataServiceCorrectly() {
        DataService mockDataService = mock(DataService.class);
        VelibStationRepository repository = new VelibStationRepository(mockDataService);

        assertThat(repository).isNotNull();
    }
}
