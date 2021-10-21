package com.troia.libraryproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequestDto implements Serializable {

    private static final long serialVersionUID = 7378195175761094251L;

    private String refreshToken;
}
