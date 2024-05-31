package open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.interfaces.rest.transform;

import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.domain.model.aggregates.Answer;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.interfaces.rest.resources.AnswerResource;
import open.source.agriculture.chaquitaclla.chaquitacllaplatformopen.forum.interfaces.rest.resources.QuestionResource;

public class AnswerResourceFromEntityAssembler {
    public static AnswerResource toResourceFromEntity(Answer entity) {
        return new AnswerResource(
                entity.getId(),
                entity.getUserId().userId(),
                entity.getQuestion(),
                entity.getAnswer()
        );

    }
}
