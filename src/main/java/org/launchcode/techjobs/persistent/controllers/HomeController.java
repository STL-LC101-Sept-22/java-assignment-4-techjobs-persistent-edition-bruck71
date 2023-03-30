package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");
        model.addAttribute("jobs", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {

        //List<Employer> employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model,
                                    @RequestParam int employerId,
                                    @RequestParam (required = false) List<Integer> skills) {

        Optional<Employer> result = employerRepository.findById(employerId);
        //Use for the error handling
        List<Employer> optEmployers = (List<Employer>) employerRepository.findAll();
        List<Skill> optSkills = (List<Skill>) skillRepository.findAll();
        if (errors.hasErrors()) {
            //Employer employers = (Employer) employerRepository.findById(employerId);
            model.addAttribute("title", "Add Job");
            model.addAttribute("employers", optEmployers);
            model.addAttribute("skills", optSkills);

            return "add";
        }
        else {
            if (skills != null) {
                List<Skill> selectedSkills = (List<Skill>) skillRepository.findAllById(skills);
                newJob.setSkills(selectedSkills);
            } else {
                return "add";
            } if (employerId == 0) {
                return "add";
            } else {
                if (result.isPresent()) {
                    Employer employer = result.get();
                    newJob.setEmployer(employer);
                }
            }
        }
        //employerRepository.save(employer);
        jobRepository.save(newJob);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
//        Optional <Job> optJob = jobRepository.findById(jobId);
        Optional optJob = jobRepository.findById(jobId); //This is built like the Employer Controller's View
        if (optJob.isPresent()) {
//            Job job = optJob.get();
            Job job = (Job) optJob.get(); //This is built like the Employer Controller's View
            model.addAttribute("job", job);
            return "view";
        } else {
            return "redirect: ";
        }
    }


}
