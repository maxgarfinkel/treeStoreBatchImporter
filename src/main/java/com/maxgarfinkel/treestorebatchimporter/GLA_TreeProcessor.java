package com.maxgarfinkel.treestorebatchimporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
@Slf4j
public class GLA_TreeProcessor implements ItemProcessor<GLA_Tree, Tree> {
    @Override
    public Tree process(final GLA_Tree gla_tree) throws Exception {
        String latinName = gla_tree.getSpecies_name();
        String commonName = gla_tree.getCommon_name().isEmpty() ||
                            gla_tree.getCommon_name().isBlank() ? gla_tree.getDisplay_name()
                            : gla_tree.getCommon_name();

        try{
            return new Tree(null,latinName,commonName,
                    new double[]{gla_tree.getLatitude(),gla_tree.getLongitude()});
        }catch (IllegalArgumentException ex){
            log.info("Invalid tree found with id: " + gla_tree.getGla_id());
            return null;
        }
    }
}
