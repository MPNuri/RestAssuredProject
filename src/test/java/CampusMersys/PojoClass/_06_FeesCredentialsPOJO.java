package CampusMersys.PojoClass;

public class _06_FeesCredentialsPOJO {

    boolean active;
    String integrationCode;
    String code;
    String feesID;
    String name;
    String priority;
    String translateName;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIntegrationCode() {
        return integrationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntegrationCode(String integrationCode) {
        this.integrationCode = integrationCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFeesID() {
        return feesID;
    }

    public void setFeesID(String feesID) {
        this.feesID = feesID;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String translateName) {
        this.translateName = translateName;
    }
}
