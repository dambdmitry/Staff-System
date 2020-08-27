package org.internship.system.files;

public class FileDistributor {
    private String path;
    private String extension;

    public FileDistributor(String path, String extension){
        this.path = path;
        this.extension = extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public DataFile getDataFileByExtension(){
        DataFile df;
        switch (extension) {
            case "txt" :
                df = new TxtDataFile();
                break;
            case "xml" :
                df = new XmlDataFile();
                break;
            case "json" :
                df = new JsonDataFile();
                break;
            default :
                df = null;
                break;
        };
        return df;
    }

    public String getPath(){
        return path;
    }
}
