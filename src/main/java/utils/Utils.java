package utils;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class Utils {

    public static String env, browser;

    static WebDriver driver;

    static Properties prop=new Properties();

    public static String getEnv(){
        if(env==null){
            env=System.getProperty("env","env1").toLowerCase();
        }
        return env;
    }

    public static String getBrowser(){
        if(browser==null){
            browser=System.getProperty("browser","chrome").toLowerCase();
        }
        return browser;
    }

    public static String getPropertyData(String getData) throws IOException {
        String path;
        switch (getEnv()){
            case "env1":
                path= new File("src/main/java/config/config_env1.properties").getAbsolutePath();
                break;
            case "env2":
                path= new File("src/main/java/config/config_env2.properties").getAbsolutePath();
                break;
            default:
                throw new IllegalStateException("Please check the env value");
        }
        FileInputStream in=new FileInputStream(path);
        prop.load(in);
        Assert.assertNotNull(prop.getProperty(getData));
        return prop.getProperty(getData);
    }

    public static void writetoProperties(String value, String name){
        env=Utils.getEnv();
        try{
            FileOutputStream myWritter = null;
            if(env.equalsIgnoreCase("env1")){
                myWritter=new FileOutputStream("src/main/java/config/config_env1.properties");
            }
            prop.setProperty(name,value);
            prop.store(myWritter,null);
        }catch (IOException e){
            System.out.println("Error occured during file writing :"+e.toString());
        }
    }

    public static JSONObject parseJSONFile(String filename) throws IOException {
        String content=new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }

    public static void responseCodeValidation(Response response, int code){
        Assert.assertEquals(response.getStatusCode(),code);
        System.out.println("Reponse code validation completed");
    }

}
