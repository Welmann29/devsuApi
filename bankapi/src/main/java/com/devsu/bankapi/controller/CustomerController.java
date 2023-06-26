package com.devsu.bankapi.controller;

import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.service.CustomerService;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.dto.general.PageableResponse;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import com.devsu.bankapi.utils.functions.decorators.LogDevsu;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.devsu.bankapi.utils.functions.CommonErrors.validationException;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService
    ){
        this.customerService = customerService;
    }

    @LogDevsu
    @Operation(summary = "Creación de nuevos clientes", description = "Es requerido que agregue el funcionario que " +
            "realizara la acción")
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CreateCustomerRequest request
    ){
        CustomerResponse res = customerService.createCustomer(request);

        if (res.getSuccess()){
            return ResponseEntity.status(201).body(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Obtención del cuente por medio de su identificación", description =
                "Se puede obtener el cliente tanto por su documento de identificación, como por el codigo interno del "
                        + "banco, para realizar busqueda por el codigo del banco enviar \"Z\" en type")
    @GetMapping
    public  ResponseEntity<CustomerResponse> getCustomerById(
            @Parameter(description = "Tipo de documento de identificacion (Z en caso se desee buscar por codigo de " +
                    "cliente", example = "D") @RequestParam String type,
            @Parameter(description = "Número de documento de identificación", example = "2070654460116")
            @RequestParam String code
    ){
        CustomerResponse res = customerService.getByCode(type, code);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Actualización de clientes registrados", description = "Se requiere conocer el numero de " +
            "funcionario que realizara la acción")
    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestBody @Valid CreateCustomerRequest request
    ){
        CustomerResponse res = customerService.updateCustomer(request);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Desactivación de un cliente registrado", description = "Para poder realizar la " +
            "desactivación de un cliente, es necesario que el balance de todas sus cuentas sea 0.00")
    @DeleteMapping
    public ResponseEntity<AbstractResponse> deleteCustomer(
            @Parameter(description = "Tipo de documento de identificacion ", example = "D") @RequestParam String type,
            @Parameter(description = "Número de documento de identificación", example = "2070654460116")
            @RequestParam String code
    ){
        AbstractResponse res = customerService.deleteCustomer(
                type, code
        );
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Reactivación de un cliente registrado")
    @PatchMapping("/reactivateCustomer")
    public  ResponseEntity<CustomerResponse> reactivate(
            @Parameter(description = "Tipo de documento de identificacion ", example = "D") @RequestParam String type,
            @Parameter(description = "Número de documento de identificación", example = "2070654460116")
            @RequestParam String code
    ){
        CustomerResponse res = customerService.reactivateCustomer(type, code);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @LogDevsu
    @Operation(summary = "Obtención por paginado o total de los clientes registrados",
            description = "Si se desean obtener todos los clientes existentes enviar 0 en ambos campos")
    @GetMapping("/findAllPageable")
    public  ResponseEntity<PageableResponse> getAllCustomers(
            @Parameter(description = "Numero de pagina deseada (Enviar 0 en ambos campos para obtener todos)")
            @RequestParam Integer page,
            @Parameter(description = "Cantidad de elementos deseados (Enviar 0 en ambos campos para obtener todos)")
            @RequestParam Integer size
    ){
        PageableResponse res = customerService.getAllPageable(page, size);
        if (res.getSuccess()){
            return ResponseEntity.ok(res);
        }else{
            return ResponseEntity.badRequest().body(res);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AbstractResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return validationException(ex);
    }

}
