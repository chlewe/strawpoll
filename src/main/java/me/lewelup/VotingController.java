package me.lewelup;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;

/**
 * Logic for the index, results, and success pages.
 *
 * @author Christian Lewe
 */
@Controller
public class VotingController {
    @Getter
    private LinkedHashSet<Option> options;

    public VotingController() {
        this.options = new LinkedHashSet<>();
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

    @RequestMapping(value = "/vote")
    public String vote(Model model, @RequestParam String name, @RequestParam String op,
            @RequestParam int choice) {
        if (name.isEmpty() || op.isEmpty()) {
            throw new IllegalArgumentException("Name or operation cannot be empty!");
        }

        for (Option option : this.options) {
            if (option.getId() == choice) {
                switch (op) {
                    case "vote":
                        option.addSupporter(name);
                        model.addAttribute("heading", "Thank you for your vote!");
                        break;
                    case "unvote":
                        option.removeSupporter(name);
                        model.addAttribute("heading", "Successfully removed your vote!");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation \'" + op + "\'!");
                }

                return "success";
            }
        }

        throw new IllegalArgumentException("Invalid choice!");
    }

    @RequestMapping(value = "/add")
    public String add(Model model, @RequestParam String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        this.getOptions().add(new Option(name));
        model.addAttribute("heading", "Successfully added the option \'" + name + "\'!");
        return "success";
    }
}
