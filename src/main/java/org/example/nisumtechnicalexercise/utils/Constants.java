package org.example.nisumtechnicalexercise.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Constants {

    public static final String CODE_SUCCESS = "00";
    public static final String CODE_DB_ERROR = "01";
    public static final String CODE_VALIDATION_ERROR = "02";
    public static final String CODE_NOT_FOUND = "03";

    public static final String MSG_SUCCESS = "Operación exitosa";
    public static final String MSG_DB_ERROR = "Error en la base de datos";
    public static final String MSG_VALIDATION_ERROR = "Error de validación";
    public static final String MSG_USER_EXISTS = "El usuario ya existe";
    public static final String MSG_USER_NOT_FOUND = "Usuario no encontrado";
    public static final String MSG_USERS_NOT_FOUND = "No se encontraron usuarios";
    public static final String MSG_USER_UPDATED = "Usuario actualizado correctamente";
    public static final String MSG_USER_DELETED = "Usuario eliminado correctamente";
    public static final String MSG_TOKEN_VALIDATED = "Validación de token completada";
}
