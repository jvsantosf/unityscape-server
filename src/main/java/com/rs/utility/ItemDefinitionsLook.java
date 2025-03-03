package com.rs.utility;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;

public class ItemDefinitionsLook {

    public static void main(String[] args) throws Exception {
        Cache.init();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final var items = Maps.newHashMap();

        for (int id = 0; id < 33205; id++) {
            ItemDefinitions definitions = ItemDefinitions.getItemDefinitions(id);
            items.put(id, new ItemDef(definitions.id, "items/sprites/" + id + ".png", definitions.name, definitions.value));
        }

        // Escreve os dados no arquivo JSON
        try (FileWriter writer = new FileWriter("items.json")) {
            gson.toJson(items, writer);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo JSON: " + e.getMessage());
        }

        System.out.println("Arquivo JSON criado com sucesso: items.json");
    }

    @Data
    @AllArgsConstructor
    public static class ItemDef {
        private int id;
        private String path;
        private String name;
        private int price;
    }
}
