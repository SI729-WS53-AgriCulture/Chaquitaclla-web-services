package open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.crops.interfaces.REST.transform;

import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.crops.domain.model.aggregates.Crop;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.crops.interfaces.REST.resources.CropResource;

public class CropResourceFromEntityAssembler {
    public static CropResource toResourceFromEntity(Crop entity) {
        return new CropResource(entity.getId(), entity.getName(), entity.getDescription(), entity.getDiseaseIds(), entity.getPestIds());
    }
}
