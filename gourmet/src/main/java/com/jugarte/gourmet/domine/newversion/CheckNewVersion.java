package com.jugarte.gourmet.domine.newversion;

import com.jugarte.gourmet.beans.LastVersion;
import com.jugarte.gourmet.data.github.GitHubDataManager;
import com.jugarte.gourmet.exceptions.ConnectionException;

import org.json.JSONException;

import java.io.IOException;

public class CheckNewVersion {

    public interface OnCheckNewVersion {
        void newVersion(LastVersion lastVersion);
    }

    public void execute(OnCheckNewVersion onCheckNewVersion) {
        try {
            LastVersion lastVersion = new GitHubDataManager().getLastVersion();
            onCheckNewVersion.newVersion(lastVersion);
        } catch (ConnectionException | JSONException | IOException e) {
            e.printStackTrace();
        }
    }

}
