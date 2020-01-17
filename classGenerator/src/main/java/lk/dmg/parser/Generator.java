package lk.dmg.parser;

import com.google.common.base.CaseFormat;
import lk.dmg.generator.*;
import lk.dmg.generator.DataHold;
import org.apache.commons.collections.map.MultiValueMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Generator {


    public static void generateClasses(String schemaPath,String templatePath, String newClassFilePath){

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(schemaPath));
            JSONObject jsonObject = (JSONObject) obj;
            MultiValueMap entites = new MultiValueMap();
            MultiValueMap map = new MultiValueMap();
            MultiValueMap[] maps = returnList(jsonObject, map, entites);
            SchemaModelGenerate generator = new SchemaModelGenerate();
            MultiValueMap mapOfAttr = maps[0];
            MultiValueMap mapOfList = maps[1];
            SchemaModelGenerate outerClassGenerator = new SchemaModelGenerate();
            Set set = jsonObject.keySet();
            for (Object keyInJson : set) {
                String segs = keyInJson.toString();
                String segmentKey = keyInJson.toString();
                String entity = (String) jsonObject.get("entity");
                ArrayList outerClassPropertyList = (ArrayList) mapOfList.get(entity);
                List<DataHold> outerClassPropertiesDataHold = new ArrayList<>();
                for (Object outerClassPorperty : outerClassPropertyList) {
                    String classListVariable = (String) outerClassPorperty;
                    String classListToCamel = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classListVariable);
                    DataHold listDataHold = new DataHold("List<" + outerClassPorperty + ">", classListToCamel + "List = new ArrayList<>()");
                    outerClassPropertiesDataHold.add(listDataHold);
                }
                outerClassGenerator.generateClass(entity, outerClassPropertiesDataHold,templatePath,newClassFilePath);
            }
            Set classesSet = mapOfAttr.keySet();
            for (Object classObj : classesSet) {
                String className = (String) classObj;
                ArrayList AttrValues = (ArrayList) mapOfAttr.get(className);
                ArrayList ListValues = (ArrayList) mapOfList.get(className);
                List<DataHold> hotelDataFields = new ArrayList<>();
                Set listKeySet = mapOfList.keySet();
                for (Object objectFromList : listKeySet) {
                    String listClassName = (String) objectFromList;
                }
                for (Object object : AttrValues) {
                    String singleAttributeName = (String) object;
                    DataHold dataHold = new DataHold("String", singleAttributeName);
                    hotelDataFields.add(dataHold);
                }
                if (ListValues != null) {
                    for (Object ListValueObj : ListValues) {
                        String classListVariable = (String) ListValueObj;
                        if (!(mapOfAttr.containsKey(classListVariable))) {
                            for (Object obje : classesSet) {
                                String classWithNoEntity = (String) obje;
                                if (classListVariable.contains(classWithNoEntity)) {
                                    String classListToCamel = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classListVariable);
                                    DataHold listDataHold = new DataHold("List<"+classWithNoEntity+">", classListToCamel + "List = new ArrayList<>()");
                                    hotelDataFields.add(listDataHold);
                                }
                            }
                        } else {
                            String classListToCamel = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classListVariable);
                            DataHold listDataHold = new DataHold("List<" + ListValueObj + ">", classListToCamel + "List = new ArrayList<>()");
                            hotelDataFields.add(listDataHold);
                        }
                    }
                }
                generator.generateClass(className, hotelDataFields,templatePath,newClassFilePath);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static MultiValueMap[] returnList(Object keyObj, MultiValueMap map, MultiValueMap entities) {

        JSONObject jObj = (JSONObject) keyObj;
        Set set = jObj.keySet();
        for (Object keySetObj : set) {
            String segmentKey = keySetObj.toString();
            String entity = (String) jObj.get("entity");
            if (segmentKey.equals("multiAttributes")) {
                JSONArray jsnObjMulti = (JSONArray) jObj.get("multiAttributes");
                for (Object multiObj : jsnObjMulti) {
                    JSONObject multiJsnObj = (JSONObject) multiObj;
                    String jsnObjInMulti = (String) multiJsnObj.get("name");
                    entities.put(entity, jsnObjInMulti);
                }
            } else
            {
                Object property = jObj.get(segmentKey);
                if (property instanceof JSONArray) {
                    JSONArray arrayProperty = (JSONArray) property;
                    for (Object o : arrayProperty) {
                        JSONObject objInArray = (JSONObject) o;
                        String jsnObj = (String) objInArray.get("name");
                        String jsnObjEnt = (String) objInArray.get("entity");
                        String forLists = "list";

                        if (jsnObj != null) {
                            map.put(entity, jsnObj);
                        }
                        if (jsnObjEnt != null) {
                            entities.put(entity, jsnObjEnt);
                            returnList(o, map, entities);
                        }
                    }
                }
            }
        }
        MultiValueMap[] arr = {map, entities};
        return arr;
    }

}
