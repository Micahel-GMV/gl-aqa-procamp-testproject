package commonLibs.utils;

public class NameManager {

    public boolean isNameAqa(String name){
        return name.matches("^aqa\\d{13}.*$");
    }

    public String getStandardName(){
        return getStandardName("");
    }

    public String getStandardName(String postfix){
        return getStandardName(System.currentTimeMillis(), postfix);
    }

    public String getStandardName(long timestamp, String postfix){
        return "aqa" + timestamp + postfix;
    }
}
