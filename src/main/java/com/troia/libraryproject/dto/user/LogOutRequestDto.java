package com.troia.libraryproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogOutRequestDto implements Serializable {

    private static final long serialVersionUID = -7300679658125770015L;

    private UUID userId;
}
