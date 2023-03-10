package dibugger.filehandler.rdbf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import dibugger.filehandler.facade.PropertiesFile;
import dibugger.filehandler.facade.PropertiesFileWriter;

/**
 * Specific implementation of {@linkplain PropertiesFileWriter} for java defined
 * Properties Files
 * 
 * @author Pascal
 *
 */
public class RDBFPropWriter extends PropertiesFileWriter {

    @Override
    public void saveProperties(PropertiesFile f) {
        Properties prop = new Properties();

        try {
            FileOutputStream fos = new FileOutputStream(PropertiesFile.DEFAULT_LOCATION);

            // Setting Properties Data
            prop.setProperty("configFileLoc", f.getConfigFile());
            prop.setProperty("language", f.getSelectedLanguage());
            prop.setProperty("maxFuncCalls", "" + f.getMaxFunctionCalls());
            prop.setProperty("maxWhileIterations", "" + f.getMaxWhileIterations());

            prop.storeToXML(fos, null);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
