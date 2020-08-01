package com.rjdiscbots.tftbot.discord.message;

import com.rjdiscbots.tftbot.db.galaxies.GalaxiesRepository;
import com.rjdiscbots.tftbot.db.galaxies.GalaxyEntity;
import com.rjdiscbots.tftbot.db.items.ItemEntity;
import com.rjdiscbots.tftbot.db.items.ItemsRepository;
import com.rjdiscbots.tftbot.db.synergies.SynergyEntity;
import com.rjdiscbots.tftbot.db.synergies.SynergyRepository;
import com.rjdiscbots.tftbot.exceptions.message.CommandDoesNotExistException;
import com.rjdiscbots.tftbot.exceptions.message.EntityDoesNotExistException;
import com.rjdiscbots.tftbot.exceptions.message.InvalidMessageException;
import com.rjdiscbots.tftbot.utility.DiscordMessageHelper;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ListMessageEventHandler {

    private ItemsRepository itemsRepository;

    private GalaxiesRepository galaxiesRepository;

    private SynergyRepository synergyRepository;

    @Autowired
    public ListMessageEventHandler(ItemsRepository itemsRepository,
        GalaxiesRepository galaxiesRepository,
        SynergyRepository synergyRepository) {
        this.itemsRepository = itemsRepository;
        this.galaxiesRepository = galaxiesRepository;
        this.synergyRepository = synergyRepository;
    }

    public void handleEmbedListMessage(@NonNull String rawListMessage,
        @NonNull EmbedBuilder embedBuilder, @NonNull Map<String, String> filePathMap)
        throws InvalidMessageException {
        if (!rawListMessage.startsWith("!list ")) {
            throw new IllegalArgumentException(
                "Message does begin with !list: " + rawListMessage);
        }

        rawListMessage = rawListMessage.replaceFirst("!list ", "");
        rawListMessage = rawListMessage.trim();

        if (rawListMessage.startsWith("galaxies")) {
            fetchAllGalaxies(embedBuilder);
        } else if (rawListMessage.startsWith("components")) {
            fetchAllComponents(embedBuilder);
        } else if (rawListMessage.startsWith("commands")) {
            fetchAllCommands(embedBuilder);
        } else if (rawListMessage.startsWith("synergies")) {
            fetchAllSynergies(embedBuilder);
        } else {
            throw new CommandDoesNotExistException("Invalid command provided!");
        }

        String picUrl = "pengu.png";
        filePathMap.put(picUrl, "patch/" + picUrl);
        embedBuilder.setThumbnail("attachment://" + picUrl);
    }

    private void fetchAllSynergies(EmbedBuilder embedBuilder) throws EntityDoesNotExistException {
        List<SynergyEntity> synergyEntities = synergyRepository.findAll();

        if (synergyEntities.isEmpty()) {
            throw new EntityDoesNotExistException("No synergies could be found!");
        }

        embedBuilder.setTitle("Synergies");
        embedBuilder.setDescription("All available synergies in TFT.");

        for (SynergyEntity synergyEntity : synergyEntities) {
            String formattedSynergyName = DiscordMessageHelper
                .formatName(synergyEntity.getName());

            String desc = synergyEntity.getDescription();

            if ((desc == null || desc.isEmpty()) && (synergyEntity.getInnate() != null
                && !synergyEntities.isEmpty())) {
                desc = synergyEntity.getInnate();
            } else if (desc == null || desc.isEmpty()) {
                desc = "No available description";
            }

            embedBuilder.addField(formattedSynergyName, desc, false);
        }
    }

    private void fetchAllCommands(EmbedBuilder embedBuilder) {
        embedBuilder.setTitle("Commands");
        embedBuilder.setDescription("All commands and their parameters.");
        embedBuilder.addField("Champion", "!champion <champion name>", false);
        embedBuilder.addField("List", "!list [galaxies, components, commands, synergies]", false);
        embedBuilder.addField("Items", "!item <item name>", false);
        embedBuilder.addField("Galaxies", "!galaxy <galaxy name>", false);
        embedBuilder.addField("Synergies", "!synergy <synergy name>", false);
        embedBuilder
            .addField("Build", "!build [--desc] <item component 1>, <item component 2>, ...",
                false);
    }

    private void fetchAllGalaxies(EmbedBuilder embedBuilder) throws EntityDoesNotExistException {
        List<GalaxyEntity> galaxiesEntities = galaxiesRepository.findAll();

        if (galaxiesEntities.isEmpty()) {
            throw new EntityDoesNotExistException("No galaxies could be found!");
        }

        embedBuilder.setTitle("Galaxies");
        embedBuilder.setTitle("All galaxies in the current patch of TFT");

        for (GalaxyEntity galaxiesEntity : galaxiesEntities) {
            String formattedGalaxyName = DiscordMessageHelper.formatName(galaxiesEntity.getName());
            embedBuilder.addField(formattedGalaxyName, galaxiesEntity.getDescripiton(), false);
        }
    }

    private void fetchAllComponents(EmbedBuilder embedBuilder) throws EntityDoesNotExistException {
        List<ItemEntity> itemEntities = itemsRepository
            .findByComponentOneIsNullAndComponentTwoIsNull();

        if (itemEntities == null || itemEntities.isEmpty()) {
            throw new EntityDoesNotExistException("No components could be found!");
        }

        embedBuilder.setTitle("Components");
        embedBuilder.setTitle("All components in the current patch of TFT");

        for (ItemEntity itemEntity : itemEntities) {
            String formattedItemName = DiscordMessageHelper.formatName(itemEntity.getName());
            embedBuilder.addField(formattedItemName, itemEntity.getDescription(), false);
        }
    }
}
