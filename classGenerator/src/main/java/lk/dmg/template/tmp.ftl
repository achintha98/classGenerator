package lk.dmg.dmgportal.schema.model.test.ha;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ${name}{

<#list classes as classOne>
    private ${classOne.typeName} ${classOne.variableName};
</#list>

