package zendesk.api;

/**
 * Represents all of the relevant ticket data returned
 * when requesting a ticket from the Zendesk API
 */
public class Ticket {

    public Ticket(){};
    // Zendesk attributes
    public boolean allowAttachments;
    public boolean allowChannelback;
    public boolean hasIncidents;
    public boolean isPublic;
    public long assigneeId;
    public long brandId;
    public long forumTopicId;
    public long groupId;
    public long id;
    public long organizationId;
    public long problemId;
    public long requesterId;
    public long submitterId;
    public long ticketFormId;
    public long viaFollowupSourceId;
    public long[] collaboratorIds;
    public long[] emailCcIds;
    public long[] followerIds;
    public long[] followupIds;
    public String createdAt;
    public String description;
    public String dueAt;
    public String externalId;
    public String priority;
    public String rawSubject;
    public String recipient;
    public String status;
    public String subject;
    public String type;
    public String updatedAt;
    public String url;
    public String[] collaborators;
    public String[] customFields;
    public String[] macroIds;
    public String[] sharingAgreementIds;
    public String[] tags;

    // Custom attributes
    public String assigneeName;
    public String requesterName;

}
