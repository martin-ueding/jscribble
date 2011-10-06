<?php
# Copyright (c) 2011 Martin Ueding <dev@martin-ueding.de>

# Reads the JSON file and generates a properties file for the Java program.

$file = file_get_contents('config/config.js');
$config = json_decode($file, true);

foreach ($config as $item) {
	echo $item['key'].'='.$item['value']."\n";
}
?>
