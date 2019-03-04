package tests;

import commonLibs.configReaders.ConfigReader;
import commonLibs.utils.NameManager;

public abstract class BaseTest {

    protected ConfigReader config;
    protected NameManager nameManager;

    protected BaseTest(){
        config = new ConfigReader();
        nameManager = new NameManager();
    }
}
