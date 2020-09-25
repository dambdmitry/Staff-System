package org.internship.system.controller;

import org.internship.system.files.FileDistributor;
import org.internship.system.staff.Staff;
import org.internship.system.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.Set;

@Deprecated
//@Controller
public class StaffController {
    private final Staff staff;

    @Autowired
    public StaffController(Staff staff) {
        this.staff = staff;
    }

    @GetMapping("/")
    public String mainForm(){
        return "mainForm";
    }

    //Метод по запросу "/printAll" вернет html файл, находящийся в webapp/WEB-INF/views
    @GetMapping("/printAll")
    public String printAll(Model model){
        Set<Worker> workers = staff.getAllWorker();
        model.addAttribute("workers", workers);
        return "printAll";
    }

    @GetMapping("/addWorker")
    public String addWorkerForm(Worker worker){
        return "addWorker";
    }

    @PostMapping("/addWorker")
    public String addWorker(Worker worker, Model model){
        if(!worker.getLastName().equals("") && !worker.getFirstName().equals("") && !worker.getPatronymic().equals("")) {
            staff.addWorker(worker);
            return "redirect:/printAll";
        }else{
            String err = "Заполните все поля";
            model.addAttribute("error", err);
            return "addWorker";
        }
    }

    @GetMapping("/removeWorker")
    public String removeWorkerForm(Worker worker){
        return "removeWorker";
    }

    @PostMapping("/removeWorker")
    public String removeWorker(@RequestParam String stringId, Model model){
        try {
            int id = Integer.parseInt(stringId); //В html застроховано, что на вход может зайти только число.
            if (staff.hasId(id)) {
                staff.remove(id);
                String msg = "Сотрудник успешно удален";
                model.addAttribute("message", msg);
                return "successfullyAction";
            } else {
                return "notFoundWorker";
            }
        } catch (NumberFormatException ex) {
            String err = "Введите id";
            model.addAttribute("error", err);
            return "removeWorker";
        }
    }

    @GetMapping("/print")
    public String getWorkerByIdForm(){
        return "getWorkerForm";
    }

    @PostMapping("/print")
    public String printWorkerById(@RequestParam String stringId, Model model) {
        try {
            int id = Integer.parseInt(stringId); //В html застроховано, что на вход может зайти только число.
            if (staff.hasId(id)) {
                Set<Worker> workers = new LinkedHashSet<>();
                workers.add(staff.getWorker(id));
                model.addAttribute("workers", workers);
                return "printAll";
            } else {
                return "notFoundWorker";
            }
        } catch (NumberFormatException ex) {
            String err = "Введите id";
            model.addAttribute("error", err);
            return "getWorkerForm";
        }
    }

    @GetMapping("/save")
    public String saveForm(FileDistributor fd){
        return "saveStaff";
    }

    @PostMapping("/save")
    public String save(FileDistributor fd, Model model){
        try {
            staff.save(fd.getPath(), fd.getDataFileByExtension());
            String msg = "Сотрудники успешно сохранены";
            model.addAttribute("message", msg);
            return "successfullyAction";
        } catch (Exception e) {
            String err = e.getMessage();
            model.addAttribute("error", err);
            return "fileError";
        }
    }

    @GetMapping("/load")
    public String loadForm(FileDistributor fd){
        return "loadStaff";
    }

    @PostMapping("/load")
    public String load(FileDistributor fd, Model model){
        try {
            staff.load(fd.getPath(), fd.getDataFileByExtension());
            String msg = "Сотрудники успешно загружены";
            model.addAttribute("message", msg);
            return "successfullyAction";
        } catch (Exception e) {
            String err = e.getMessage();
            model.addAttribute("error", err);
            return "fileError";
        }
    }

}
