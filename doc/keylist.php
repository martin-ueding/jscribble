<?php
# Copyright © 2011-2012 Martin Ueding <dev@martin-ueding.de>
# Parse the default config file and insert the comments and default value
# here.
echo "\n";

$file = file_get_contents('config/config.js');
$config = json_decode($file, true);

foreach ($config as $item) {
	echo "``".$item['key']."``\n\t";
	if (!empty($item['comment']))
		echo $item['comment']."\n";
	echo "\t[".$item['type']."] (".$item['value'].")\n";
}
?>
