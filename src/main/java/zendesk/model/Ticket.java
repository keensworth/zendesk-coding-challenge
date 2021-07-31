package zendesk.model;

import java.util.List;

public class Ticket {

    public Ticket(){};

    boolean allowAttachments;
    boolean allowChannelback;
    boolean hasIncidents;
    boolean isPublic;
    int assigneeId;
    int brandId;
    int forumTopicId;
    int groupId;
    int id;
    int organizationId;
    int problemId;
    int requesterId;
    int submitterId;
    int ticketFormId;
    int viaFollowupSourceId;
    int[] collaboratorIds;
    int[] emailCcIds;
    int[] followerIds;
    int[] followupIds;
    String createdAt;
    String description;
    String dueAt;
    String externalId;
    String priority;
    String rawSubject;
    String recipient;
    String satisfactionRating;
    String status;
    String subject;
    String type;
    String updatedAt;
    String url;
    String via;
    String[] collaborators;
    String[] customFields;
    String[] macroIds;
    String[] sharingAgreementIds;
    String[] tags;
}
