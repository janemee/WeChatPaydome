package util;



import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtil {

    private static final Logger LOGGER = Logger.getLogger(FreemarkerUtil.class);

    public static Configuration config;

    public static String renderTemplate(String s, Map<String, Object> data) throws IOException, TemplateException {
        Template t = new Template(null, new StringReader(s), config);
        // 执行插值，并输出到指定的输出流中
        StringWriter w = new StringWriter();
        t.getConfiguration();
        try {
            t.process(data, w);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(data.get("noticeTime"));
        }

        return w.getBuffer().toString();
    }

    public static String renderFileTemplate(String file, Map<String, Object> data) throws IOException,
            TemplateException {
        Configuration cfg = config;
        cfg.setDefaultEncoding("UTF-8");
        // 取得模板文件
        Template t = cfg.getTemplate(file);
        // 执行插值，并输出到指定的输出流中
        StringWriter w = new StringWriter();
        t.getConfiguration();
        t.process(data, w);
        return w.getBuffer().toString();
    }

}
