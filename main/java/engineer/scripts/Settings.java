package engineer.scripts;

import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Settings extends ModSettings {
    public boolean wrenchGrid = true;

    @Override
    public void addSaveData(SaveData save) {
        save.addBoolean("wrenchGrid", wrenchGrid,"Enable/Disable wrench grid.\n                               // false = disabled.\n                               // true = enabled.");
    }

    @Override
    public void applyLoadData(LoadData save) {
        if (save == null)
            return;
        wrenchGrid = save.getBoolean("wrenchGrid",true);
    }


}