package com.example.AplicationWeb.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.AplicationWeb.Servicio.ProductoServicio;
import com.example.AplicationWeb.Repositorio.ProductoRepositorio;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Productos")
public class ProductoControlador {

    @Autowired
    private ProductoRepositorio repositorio;

    // Crear un nuevo producto
    @PostMapping("/crear")
    public ResponseEntity<ProductoServicio> createProducto(@RequestBody ProductoServicio producto) {
        try {
            ProductoServicio savedProducto = repositorio.save(producto);
            return new ResponseEntity<>(savedProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los productos con paginación si hay más de 10 productos
    @GetMapping
    public ResponseEntity<List<ProductoServicio>> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<ProductoServicio> pageProductos = repositorio.findAll(paging);

            if (pageProductos.hasContent()) {
                return new ResponseEntity<>(pageProductos.getContent(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un producto por su ID
    @GetMapping("/leer/{id}")
    public ResponseEntity<ProductoServicio> getProductoById(@PathVariable("id") String id) {
        Optional<ProductoServicio> productoData = repositorio.findById(id);

        if (productoData.isPresent()) {
            return new ResponseEntity<>(productoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un producto por su ID
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoServicio> updateProducto(@PathVariable("id") String id, @RequestBody ProductoServicio producto) {
        Optional<ProductoServicio> productoData = repositorio.findById(id);

        if (productoData.isPresent()) {
            ProductoServicio existingProducto = productoData.get();
            existingProducto.setNombre(producto.getNombre());
            existingProducto.setPrecio(producto.getPrecio());
            existingProducto.setDescripcion(producto.getDescripcion());
            ProductoServicio updatedProducto = repositorio.save(existingProducto);
            return new ResponseEntity<>(updatedProducto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un producto por su ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpStatus> deleteProducto(@PathVariable("id") String id, @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar todos los productos
    @DeleteMapping("/eliminar/todo")
    public ResponseEntity<HttpStatus> deleteAllProductos(@RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            repositorio.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Manejar rutas no existentes
    @RequestMapping("*")
    public ResponseEntity<String> handleInvalidRoutes() {
        return new ResponseEntity<>("Error: Endpoint no encontrado.", HttpStatus.NOT_FOUND);
    }
}

