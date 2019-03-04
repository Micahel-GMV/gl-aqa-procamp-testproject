package commonLibs.configReaders;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader { //Class isn`t static `cause it`s needed sometimes to have different sets of properties.
    //In case of simple config it`s useful to use a pack of static finals that could be accessed
    // like String uri = ConfigReader.URI;

    private static final Logger log = LogManager.getLogger(ConfigReader.class);

    private static final String CONFIG_PATH = Paths.get("").toAbsolutePath() + File.separator + "config" + File.separator;
    private Properties properties;

    //public static final ConfigReader config = new ConfigReader();//I don`t really know will it be needed to have
    // different configs but I`m sure it`ll be useful to have default one
    public ConfigReader(){
        properties = new Properties();
        String propertiesFilePath = CONFIG_PATH;
        switch (getEnv()){
            case "dev": propertiesFilePath += "dev_env.properties";
                break;
            case "prod": propertiesFilePath += "qa_env.properties";
                break;
            default: {
                String errorMessage = "Unknown env value " + getEnv() + " .";
                log.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        }
        try {
            properties.load(new FileInputStream(propertiesFilePath));
        }
        catch (Exception e){
            log.error("Couldn`t read properties. Exiting.", e);
            System.exit(1);
        }

        log.info("Setting base port and uri " + getUri() + ":" + getPort());
        RestAssured.baseURI = getUri();
        RestAssured.port = getPort();
    }

    public String getEnv(){
        return "dev";//TODO: make it reading from pom or env vars
    }

    public String getUri(){
        return properties.getProperty("url");
    }

    public int getPort(){
        return Integer.valueOf(properties.getProperty("port"));
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

    public String getConfigFileAsString(String fileName){
        try( BufferedReader br =
                     new BufferedReader( new InputStreamReader(new FileInputStream(CONFIG_PATH + fileName), "UTF-8" )))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
        catch (IOException exception){
            log.error("Couldn`t read application.xsd ");
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String propertiesFilePath = Paths.get("").toAbsolutePath() + File.separator + "config" + File.separator + "runtime.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFilePath));
        System.out.println(properties.getProperty("env"));
    }
}
