/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DTO.DashboardDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import servicio.DashboardServicio;

@Controller
public class DashboardControlador {

    private final DashboardServicio dashboardService;

    public DashboardControlador(DashboardServicio dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        DashboardDTO datos =
                dashboardService.obtenerDashboard();

        model.addAttribute("datos", datos);

        return "dashboard";
    }
}
