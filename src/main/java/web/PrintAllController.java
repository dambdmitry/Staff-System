package web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import staff.Staff;
import staff.Worker;

@Controller
public class PrintAllController {
    private Staff staff = new Staff();

    //Метод по запросу "/printAll" вернет html файл, находящийся в webapp/WEB-INF/views
    @GetMapping("/printAll")
    public String printAll(Model model){
        model.addAttribute("all", getHtmlCodeOfAllWorkers());
        return "printAll";
    }

    private String getHtmlCodeOfAllWorkers(){
        StringBuilder sb = new StringBuilder();
        for(Worker worker: staff.getAllWorker()){
            sb.append(worker).append("<br>");
        }
        return sb.toString();
    }
}
