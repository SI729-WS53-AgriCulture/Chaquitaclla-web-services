package open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.application.internal.commandservices;

import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.model.aggregates.Sowing;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.model.commands.CreateSowingControlCommand;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.model.entities.SowingControl;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.domain.services.SowingControlCommandService;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.infrastructure.persistence.jpa.repositories.SowingControlRepository;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.sowings.infrastructure.persistence.jpa.repositories.SowingRepository;
import org.springframework.stereotype.Service;

@Service
public class SowingControlCommandServiceImpl implements SowingControlCommandService {

    private final SowingRepository sowingRepository;
    private final SowingControlRepository sowingControlsRepository;

    public SowingControlCommandServiceImpl(SowingRepository sowingRepository, SowingControlRepository sowingControlsRepository) {
        this.sowingRepository = sowingRepository;
        this.sowingControlsRepository = sowingControlsRepository;
    }

    @Override
    public Long handle(CreateSowingControlCommand command) {
        Sowing sowing = sowingRepository.findById(command.sowingId())
                .orElseThrow(() -> new IllegalArgumentException("Sowing with id " + command.sowingId() + " does not exist"));

        var sowingControl = new SowingControl(sowing, command.sowingCondition(), command.sowingSoilMoisture(), command.sowingStemCondition());
        sowingControlsRepository.save(sowingControl);
        return sowingControl.getId();
    }
}