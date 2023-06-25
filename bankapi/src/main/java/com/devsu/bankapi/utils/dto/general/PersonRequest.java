package com.devsu.bankapi.utils.dto.general;

import com.devsu.bankapi.utils.abstracts.AdministrativeAbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest extends AdministrativeAbstractRequest {

    @NotBlank(message = "Es requerido conocer el tipo de documento de identificacion")
    @Pattern(regexp = "^(D|P)", message = "El tipo ingresado no es correcto")
    @Schema(description = "D - Documento Personal de Identificaación, P - Pasaporte")
    private String documentType;

    @NotBlank(message = "Es obligatorio ingresar el documento de identificación")
    @Size(max = 20, message = "Supera el número de caracteres validos")
    private String document;

    @NotBlank(message = "Es obligatorio ingresar el primer nombre")
    @Size(max = 18, message = "Supera el número de caracteres validos (18)")
    private String firstName;

    @Size(max = 18, message = "Supera el número de caracteres validos (18)")
    private String middleName;

    @NotBlank(message = "Es obligatorio ingresar el primer apellido")
    @Size(max = 18, message = "Supera el número de caracteres validos (18)")
    private String firstSurname;

    @Size(max = 18, message = "Supera el número de caracteres validos (18)")
    private String secondSurname;

    @NotBlank(message = "Es obligatrorio ingresa el genero")
    @Pattern(regexp = "^(M|F)", message = "El genero ingresado es incorrecto")
    private String gender;

    @NotBlank(message = "Es obligatrorio ingresar la dirección")
    @Size(max = 250, message = "Supera el número de caracteres validos (250)")
    private String direction;

    private String internationalCallingCode;

    @NotBlank(message = "Es obligatorio ingresar el número de celular")
    @Pattern(regexp = "^([0-9]*)", message = "Solo se aceptan digitos")
    @Size(max = 20, message = "Supera el número de caracteres validos (20)")
    private String phoneNumber;

}
