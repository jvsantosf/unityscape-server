package com.rs.utility.tools;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ObjectDefinitions;

import java.util.Arrays;

public class ObjectAnalyze {

    public static void main(String[] args) throws Exception {
        Cache.init();

        ObjectDefinitions definitions = ObjectDefinitions.getObjectDefinitions(2733);
        System.out.println(definitions.name);
        for (int[] modelId : definitions.modelIds) {
            System.out.println(Arrays.toString(modelId));
        }

    }

}
