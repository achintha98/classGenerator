package lk.dmg.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaModelGenerate {
    private String className;
    private DataHold dataHold;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public DataHold getDataHold() {
        return dataHold;
    }

    public void setDataHold(DataHold dataHold) {
        this.dataHold = dataHold;
    }

    public void generateClass(String className, List<DataHold> dataHold, String templatePath, String newClassPath){

        //Freemarker configuration object
        Configuration cfg = new Configuration();
        try {
            //Load template from source folder
            Template template = cfg.getTemplate(templatePath);

            // Build the data-model
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", className);
            Writer file = new FileWriter(new File(newClassPath+className+".java"));
            List<DataHold> classes = dataHold;
            data.put("classes", classes);
            template.process(data, file);
            file.flush();
            file.close();

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
