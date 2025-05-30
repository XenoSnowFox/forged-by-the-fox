package com.xenosnowfox.forgedbythefox.persistence;

import com.xenosnowfox.forgedbythefox.models.campaign.Campaign;
import com.xenosnowfox.forgedbythefox.models.campaign.CampaignIdentifier;

public interface CampaignRepository {
    Campaign retrieve(CampaignIdentifier withIdentifier);
}
