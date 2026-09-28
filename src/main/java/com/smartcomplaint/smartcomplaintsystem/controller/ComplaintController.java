package com.smartcomplaint.smartcomplaintsystem.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartcomplaint.smartcomplaintsystem.model.Complaint;
import com.smartcomplaint.smartcomplaintsystem.repository.ComplaintRepository;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @PostMapping("/submit-complaint")
    public String submitComplaint(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam String severity,
        HttpSession session) {

        Complaint complaint = new Complaint();

        complaint.setTitle(title);
        complaint.setCategory(category);
        complaint.setDescription(description);
        complaint.setLocation(location);
        complaint.setSeverity(severity);
        String userEmail = (String)
        session.getAttribute("userEmail");
        complaint.setUserEmail(userEmail);
        int priorityScore;

if (severity.equalsIgnoreCase("High")) {
    priorityScore = 5;
} else if (severity.equalsIgnoreCase("Medium")) {
    priorityScore = 3;
} else {
    priorityScore = 1;
}

complaint.setPriorityScore(priorityScore);

if (priorityScore >= 5) {
    complaint.setPriority("HIGH");
} else if (priorityScore >= 3) {
    complaint.setPriority("MEDIUM");
} else {
    complaint.setPriority("LOW");
}


        complaintRepository.save(complaint);

        return "redirect:/dashboard.html";
    }
    
    

    @GetMapping("/api/complaints")
    @ResponseBody
    public java.util.List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }
    @GetMapping("/api/my-complaints")
@ResponseBody
public java.util.List<Complaint> getMyComplaints(HttpSession session) {

    String userEmail = (String) session.getAttribute("userEmail");

    return complaintRepository.findByUserEmail(userEmail);
}

    @PostMapping("/api/complaints/{id}/status")
@ResponseBody
public String updateStatus(
        @PathVariable Long id,
        @RequestParam String status) {

    Complaint complaint = complaintRepository.findById(id).orElse(null);

    if (complaint == null) {
        return "Complaint not found";
    }

    complaint.setStatus(status);
    complaintRepository.save(complaint);

    return "Status updated successfully";
}

}


