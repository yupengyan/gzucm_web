package cn.edu.gzucm.web.utils;

public class DownloadFileBean {
    private String sSiteURL; //Site's URL 
    private String sFilePath; //Saved File's Path 
    private String sFileName; //Saved File's Name 
    private int nSplitter; //Count of Splited Downloading File 

    public DownloadFileBean() {

        this("", "", "", 5);
    }

    public DownloadFileBean(String sURL, String sPath, String sName, int nSpiltter) {

        sSiteURL = sURL;
        sFilePath = sPath;
        sFileName = sName;
        this.nSplitter = nSpiltter;
    }

    public String getSSiteURL() {

        return sSiteURL;
    }

    public void setSSiteURL(String value) {

        sSiteURL = value;
    }

    public String getSFilePath() {

        return sFilePath;
    }

    public void setSFilePath(String value) {

        sFilePath = value;
    }

    public String getSFileName() {

        return sFileName;
    }

    public void setSFileName(String value) {

        sFileName = value;
    }

    public int getNSplitter() {

        return nSplitter;
    }

    public void setNSplitter(int nCount) {

        nSplitter = nCount;
    }
}
