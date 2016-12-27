package me.lewelup.controller;

import lombok.Getter;
import lombok.NonNull;
import me.lewelup.model.Option;
import me.lewelup.model.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.Map;
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

    private Option findOptionById(@NonNull Iterable<Option> iterable, @NonNull long id) {
        for (Option option : iterable) {
            if (option.getId() == id) {
                return option;
            }
        }

        return null;
    }

    @RequestMapping("/vote")
    public String vote(Model model, @RequestParam Map<String, String> params) {
        if (!params.containsKey("name") || params.get("name").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        if (!params.containsKey("op") || params.get("op").isEmpty()) {
            throw new IllegalArgumentException("Operation cannot be empty!");
        }

        boolean oneSuccessful = false;
        boolean oneFailed = false;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals("name") || entry.getKey().equals("name") ||
                    !entry.getValue().equals("on")) {
                continue;
            }

            Option option = findOptionById(this.options, Long.valueOf(entry.getKey()));

            if (option == null) {
                throw new IllegalArgumentException("Chosen option doesn't exist! (ID = \'" +
                        entry.getKey() + "\')");
            }

            switch (params.get("op")) {
                case "vote":
                    if (option.addSupporter(params.get("name"))) {
                        oneSuccessful = true;
                    } else {
                        oneFailed = true;
                    }

                    break;
                case "unvote":
                    if (option.removeSupporter(params.get("name"))) {
                        oneSuccessful = true;
                    } else {
                        oneFailed = true;
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation \'" + params.get("op")
                            + "\'!");
            }

            this.repository.save(option);
        }

        switch (params.get("op")) {
            case "vote":
                if (!oneFailed) {
                    model.addAttribute("heading", "Thank you for your votes!");
                } else if (oneSuccessful) {
                    model.addAttribute("heading" , "You have already voted for some of these " +
                            "options! These votes were ignored.");
                } else {
                    model.addAttribute("heading", "You have already voted for all of these " +
                            "options! No votes have been added.");
                }

                break;
            case "unvote":
                if (!oneFailed) {
                    model.addAttribute("heading", "Successfully removed all of these votes!");
                } else if (oneSuccessful) {
                    model.addAttribute("heading", "Some of these options don't exist! These " +
                            "votes weren't removed.");
                } else {
                    model.addAttribute("heading", "None of these options exist! No votes have " +
                            "been removed.");
                }

                break;
            default:
                throw new IllegalArgumentException("Invalid operation \'" + params.get("op")
                        + "\'!");
        }

        return "success";
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

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("options", this.options);
        return "admin";
    }

    @RequestMapping("/admin/delete")
    public String adminDelete(Model model, @RequestParam long id) {
        for (Option option : this.options) {
            if (option.getId() == id) {
                if (this.options.remove(option)) {
                    this.repository.delete(id);
                    model.addAttribute("heading", "Successfully deleted \'" + option.getName() +
                            "\'!");
                } else {
                    model.addAttribute("heading", "Failed to delete non-existent option!");
                }

                model.addAttribute("main", "../../");
                model.addAttribute("results", "../../results/");

                return "success";
            }
        }

        throw new IllegalArgumentException("Chosen option doesn't exist! (Invalid ID)");
    }
}
