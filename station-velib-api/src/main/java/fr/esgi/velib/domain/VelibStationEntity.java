package fr.esgi.velib.domain;

public class VelibStationEntity {
    private String stationCode;
    private String name;
    private String isInstalled;
    private int capacity;
    private int docksAvailable;
    private int bikesAvailable;
    private int mechanical;
    private int eBike;
    private String isRenting;
    private String isReturning;
    private String dueDate;
    private double longitude;
    private double latitude;
    private String nomCommune;
    private String codeInseeCommune;
    private String stationOpeningHours;

    public VelibStationEntity() {}

    public VelibStationEntity(String stationcode, String name, String isInstalled,
                              int capacity, int numdocksavailable, int numbikesavailable,
                              int mechanical, int ebike, String isRenting, String isReturning,
                              String duedate, double longitude, double latitude,
                              String nomCommune, String codeInseeCommune, String stationOpeningHours) {
        this.stationCode = stationcode;
        this.name = name;
        this.isInstalled = isInstalled;
        this.capacity = capacity;
        this.docksAvailable = numdocksavailable;
        this.bikesAvailable = numbikesavailable;
        this.mechanical = mechanical;
        this.eBike = ebike;
        this.isRenting = isRenting;
        this.isReturning = isReturning;
        this.dueDate = duedate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nomCommune = nomCommune;
        this.codeInseeCommune = codeInseeCommune;
        this.stationOpeningHours = stationOpeningHours;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getName() {
        return name;
    }

    public String getIsInstalled() {
        return isInstalled;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDocksAvailable() {
        return docksAvailable;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    public int getMechanical() {
        return mechanical;
    }

    public int geteBike() {
        return eBike;
    }

    public String getIsRenting() {
        return isRenting;
    }

    public String getIsReturning() {
        return isReturning;
    }

    public String getDueDate() {
        return dueDate;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public String getCodeInseeCommune() {
        return codeInseeCommune;
    }

    public String getStationOpeningHours() {
        return stationOpeningHours;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsInstalled(String isInstalled) {
        this.isInstalled = isInstalled;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDocksAvailable(int docksAvailable) {
        this.docksAvailable = docksAvailable;
    }

    public void setBikesAvailable(int bikesAvailable) {
        this.bikesAvailable = bikesAvailable;
    }

    public void setMechanical(int mechanical) {
        this.mechanical = mechanical;
    }

    public void seteBike(int eBike) {
        this.eBike = eBike;
    }

    public void setIsRenting(String isRenting) {
        this.isRenting = isRenting;
    }

    public void setIsReturning(String isReturning) {
        this.isReturning = isReturning;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public void setCodeInseeCommune(String codeInseeCommune) {
        this.codeInseeCommune = codeInseeCommune;
    }

    public void setStationOpeningHours(String stationOpeningHours) {
        this.stationOpeningHours = stationOpeningHours;
    }
}
