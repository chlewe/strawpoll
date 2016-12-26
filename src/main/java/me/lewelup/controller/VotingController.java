package me.lewelup.controller;

import lombok.Getter;
import me.lewelup.model.Option;
import me.lewelup.model.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Logic for the index, results, and success pages.
 *
 * @author Christian Lewe
 */
@Controller
public class VotingController {
    @Getter
    private Set<Option> options;
    private OptionRepository repository;

    @Autowired
    public VotingController(OptionRepository repository) {
        this.repository = repository;
        this.options = new LinkedHashSet<>();

        for (Option option : this.repository.findAll()) {
            this.options.add(option);
        }
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("options", this.options);
        return "index";
    }

    @RequestMapping("/results")
    public String results(Model model) {
        model.addAttribute("options", this.options);
        return "results";
    }

    @RequestMapping("/vote")
    public String vote(Model model, @RequestParam String name, @RequestParam String op,
            @RequestParam long id) {
        if (name.isEmpty() || op.isEmpty()) {
            throw new IllegalArgumentException("Name or operation cannot be empty!");
        }

        for (Option option : this.options) {
            if (option.getId() == id) {
                switch (op) {
                    case "vote":
                        if (option.addSupporter(name)) {
                            model.addAttribute("heading", "Thank you for your vote!");
                        } else {
                            model.addAttribute("heading", "You already voted for this option!");
                        }

                        break;
                    case "unvote":
                        if (option.removeSupporter(name)) {
                            model.addAttribute("heading", "Successfully removed your vote!");
                        } else {
                            model.addAttribute("heading", "Failed to remove non-existent vote!");
                        }

                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation \'" + op + "\'!");
                }

                this.repository.save(option);
                return "success";
            }
        }

        throw new IllegalArgumentException("Chosen option doesn't exist! (Invalid ID)");
    }

    @RequestMapping("/add")
    public String add(Model model, @RequestParam String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        Option newOption = new Option(name);

        if (this.getOptions().add(newOption)) {
            this.repository.save(newOption);
            model.addAttribute("heading", "Successfully added \'" + name + "\' to the poll!");
        } else {
            model.addAttribute("heading", "This option already exists!");
        }

        return "success";
    }
}
