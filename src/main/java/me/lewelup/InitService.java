package me.lewelup;

import me.lewelup.controller.VotingController;
import me.lewelup.model.Option;
import me.lewelup.model.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Configures the Components after Spring has launched.
 *
 * @author Christian Lewe
 */
@Service
public class InitService {
    private VotingController votingController;
    private OptionRepository repository;

    @Autowired
    public InitService(VotingController votingController, OptionRepository repository) {
        this.votingController = votingController;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        Set<Option> options = this.votingController.getOptions();

        options.add(new Option("File I/O"));
        options.add(new Option("Multi-file management"));
        options.add(new Option("Debugging"));
        options.add(new Option("SDL library"));
        options.add(new Option("ncurses library"));
        options.add(new Option("Linked lists"));
        options.add(new Option("Sending emails"));

        for (Option option : options) {
            this.repository.save(option);
        }
    }
}
