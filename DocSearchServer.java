import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelpers {
    static List<File> getFiles(Path start) throws IOException {
        File f = start.toFile();
        List<File> result = new ArrayList<>();
        if(f.isDirectory()) {
            System.out.println("It's a folder");
            File[] paths = f.listFiles();
            for(File subFile: paths) {
                result.addAll(getFiles(subFile.toPath()));
            }
        }
        else {
            result.add(start.toFile());
        }
        return result;
    }
    static String readFile(File f) throws IOException {
        System.out.println(f.toString());
        return new String(Files.readAllBytes(f.toPath()));
    }
}

class Handler implements URLHandler {
    List<File> files;
    Handler(String directory) throws IOException {
      this.files = FileHelpers.getFiles(Paths.get(directory));
    }
    public String handleRequest(URI url) throws IOException {
        Path start = Paths.get(url); 
        List<File> files = FileHelpers.getFiles(start); 
      if(url.getPath().equals("/")){
        int num = this.files.size(); 
        return "There are " +  num + " files to search"; 
      }
      else{
        if(url.getPath().contains("/search")){
            String[] parameters = url.getQuery().split("="); 
            String strToFind = parameters[1]; 
            List<File> eachFile = new ArrayList<>(); 
            //getting list of Files with the search word
            for(File f: files){
                if(FileHelpers.readFile(f).contains(strToFind)){
                    eachFile.add(f); 
                }
            }

            //converting list into a string list that can be printed 
            
            return "There were " + eachFile.size() + " files found" + "/n" + eachFile.toString(); 
        }
      }

        return "empty"; 
    }
}

class DocSearchServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler("./technical/"));
    }
}

