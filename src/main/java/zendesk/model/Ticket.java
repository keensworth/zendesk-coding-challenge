package zendesk.model;

/**
 * Represents all of the relevant ticket data returned
 * when requesting a ticket from the Zendesk API
 */
public class Ticket {

    public Ticket(){};

    boolean allowAttachments;
    boolean allowChannelback;
    boolean hasIncidents;
    boolean isPublic;
    long assigneeId;
    long brandId;
    long forumTopicId;
    long groupId;
    long id;
    long organizationId;
    long problemId;
    long requesterId;
    long submitterId;
    long ticketFormId;
    long viaFollowupSourceId;
    long[] collaboratorIds;
    long[] emailCcIds;
    long[] followerIds;
    long[] followupIds;
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
