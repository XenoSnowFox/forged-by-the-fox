package com.xenosnowfox.forgedbythefox.models.campaign;

public record CampaignName(String value) {

    public static final int MAX_LENGTH = 50;

    public static final CampaignName UNKNOWN = new CampaignName("Untitled Campaign");

    public CampaignName {
        value = value.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Campaign Name cannot be blank or empty.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Campaign Name cannot be greater than %s characters in length.".formatted(MAX_LENGTH));
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
