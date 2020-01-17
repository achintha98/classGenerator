package lk.dmg.generator;

public class DataHold {
    private String typeName;
    private String variableName;

    public DataHold(String className, String variableName){
        this.typeName = className;
        this.variableName = variableName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}