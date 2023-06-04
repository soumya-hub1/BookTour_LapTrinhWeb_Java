package hieukientung.booktour.controller;

import hieukientung.booktour.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @GetMapping("/delete-tour/{id}")
    public String deleteTour(@PathVariable("id") Long id) {
        adminService.deleteTour(id);
        return "admin";
    }
}
