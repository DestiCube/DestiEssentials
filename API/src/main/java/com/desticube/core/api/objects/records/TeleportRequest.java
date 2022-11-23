package com.desticube.core.api.objects.records;

import com.desticube.core.api.enums.RequestType;
import com.desticube.core.api.objects.DestiPlayer;

public record TeleportRequest(DestiPlayer sender, DestiPlayer reciever, RequestType type) {
}
