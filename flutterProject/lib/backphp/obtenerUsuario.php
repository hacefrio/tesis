<?php

    require "conexion.php";
    
    $usuario = $_POST['usuario'];
    $contrasena = $_POST['contrasena'];
    
    // PRUEBAS
    //$usuario = "sergio";
    //$contrasena = "sergio";
    
    $sql = "SELECT * FROM user WHERE name='$usuario' AND password='$contrasena'";
    $query = $mysqli->query($sql);
    
    if($query->num_rows > 0) {
        echo "CORRECTO";
    } else {
        echo "ERROR";
    }
    
?>
