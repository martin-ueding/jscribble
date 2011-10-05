_jscribble() {
	local cur
	COMPREPLY=()
	cur=${COMP_WORDS[COMP_CWORD]}

	case "$cur" in
		*)
			COMPREPLY=( $(compgen -W '-v' -- $cur) \
				$(compgen -W '
<?php
$file = file('jscribble/default_config.properties');

foreach ($file as $line) {
	$line = trim($line);
	if (strlen($line) == 0 || $line[0] == '#') {
		continue;
	}

	$key_value = trim($lines[$i]);
	$key_value_array = explode('=', $key_value);
	$key = $key_value_array[0];

	echo $key;
	echo ' ';
}

?>
				' -- $cur)
			)
			;;
	esac

	return 0
}

complete -F _jscribble jscribble

# vim: ft=sh
