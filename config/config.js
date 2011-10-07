[
{
	"type": "String",
	"comment": "Date format used for logging.",
	"key": "date_format",
	"value": "yyyy-MM-dd HH:mm:ss"
},
{
	"type": "Color",
	"comment": "Background color of the online help screen. This is a translucent black currently.",
	"key": "help_screen_background_color",
	"value": "C8000000"
},
{
	"type": "Integer",
	"comment": "Border radius on the backdrop of the online help screen.",
	"key": "help_screen_border_radius",
	"value": "20"
},
{
	"type": "Key",
	"comment": "Closes the online help screen.",
	"key": "help_screen_close_key",
	"value": "27"
},
{
	"type": "Integer",
	"comment": "Margin outside the backdrop.",
	"key": "help_screen_margin",
	"value": "50"
},
{
	"type": "Integer",
	"comment": "Padding inside the backdrop.",
	"key": "help_screen_padding",
	"value": "70"
},
{
	"type": "Integer",
	"comment": "Spacing between left and right column of the help screen.",
	"key": "help_screen_spacing",
	"value": "250"
},
{
	"type": "Key",
	"comment": "Toggles the online help screen.",
	"key": "help_screen_toggle_key",
	"value": "h,112"
},
{
	"type": "Integer",
	"comment": "Spacing between the rows of the help screen.",
	"key": "help_screen_vspacing",
	"value": "30"
},
{
	"type": "Color",
	"comment": "Color of the help splash. This is a very translucent black currently.",
	"key": "help_splash_background_color",
	"value": "64000000"
},
{
	"type": "Integer",
	"comment": "",
	"key": "help_splash_border_radius",
	"value": "20"
},
{
	"type": "Boolean",
	"comment": "Enables the help splash at startup.",
	"key": "help_splash_enable",
	"value": "true"
},
{
	"type": "Integer",
	"comment": "",
	"key": "help_splash_height",
	"value": "50"
},
{
	"type": "Integer",
	"comment": "",
	"key": "help_splash_margin",
	"value": "15"
},
{
	"type": "Integer",
	"comment": "",
	"key": "memory_usage_position_bottom",
	"value": "10"
},
{
	"type": "Integer",
	"comment": "",
	"key": "memory_usage_position_left",
	"value": "10"
},
{
	"type": "Boolean",
	"comment": "Enables the memory usage display.",
	"key": "memory_usage_show",
	"value": "false"
},
{
	"type": "Color",
	"comment": "Color of the paper.",
	"key": "notebook_background_color",
	"value": "FFFFFF"
},
{
	"type": "Integer",
	"comment": "Number of notesheets that are to be cached when moving back and forward in the notebook.",
	"key": "notebook_cache_width",
	"value": "10"
},
{
	"type": "Key",
	"comment": "Closes the current notebook.",
	"key": "notebook_close_key",
	"value": "q"
},
{
	"type": "Integer",
	"comment": "Height of new notebooks if the user does not use his native resolution.",
	"key": "notebook_default_height",
	"value": "600"
},
{
	"type": "Integer",
	"comment": "Width of new notebooks if the user does not use his native resolution",
	"key": "notebook_default_width",
	"value": "1024"
},
{
	"type": "Integer",
	"comment": "",
	"key": "notebook_draw_mouse_button",
	"value": "0,1024"
},
{
	"type": "Integer",
	"comment": "Width of the pen.",
	"key": "notebook_draw_stroke_width",
	"value": "1"
},
{
	"type": "Boolean",
	"comment": "Enables the eraser.",
	"key": "notebook_erase_enable",
	"value": "true"
},
{
	"type": "Mouse",
	"comment": "",
	"key": "notebook_erase_mouse_button",
	"value": "256,4096"
},
{
	"type": "Integer",
	"comment": "Width of the eraser. It should be way larger than the pen to make erasing easy.",
	"key": "notebook_erase_stroke_width",
	"value": "8"
},
{
	"type": "Integer",
	"comment": "Time in milliseconds to wait after last erasing to repaint the onion layers and ruling.",
	"key": "notebook_erase_timeout",
	"value": "100"
},
{
	"type": "Color",
	"comment": "Color of the pen.",
	"key": "notebook_foreground_color",
	"value": "000000"
},
{
	"type": "Key",
	"comment": "",
	"key": "notebook_go_back_key",
	"value": "k,38,37,08"
},
{
	"type": "Mouse",
	"comment": "",
	"key": "notebook_go_back_mouse_button",
	"value": ""
},
{
	"type": "Key",
	"comment": "",
	"key": "notebook_go_forward_key",
	"value": "j,40,39,32,10"
},
{
	"type": "Mouse",
	"comment": "",
	"key": "notebook_go_forward_mouse_button",
	"value": ""
},
{
	"type": "Key",
	"comment": "",
	"key": "notebook_goto_first_key",
	"value": "f,36"
},
{
	"type": "Key",
	"comment": "",
	"key": "notebook_goto_last_key",
	"value": "l,35"
},
{
	"type": "String",
	"comment": "The name of every new notebook is validated against this. This prevents anything cumbersome in file names.",
	"key": "notebook_name_validation_pattern",
	"value": "[A-Za-z0-9-_]+"
},
{
	"type": "Integer",
	"comment": "",
	"key": "notebook_selection_window_height",
	"value": "300"
},
{
	"type": "Integer",
	"comment": "",
	"key": "notebook_selection_window_width",
	"value": "400"
},
{
	"type": "Integer",
	"comment": "",
	"key": "onion_info_position_left",
	"value": "10"
},
{
	"type": "Integer",
	"comment": "",
	"key": "onion_info_position_top",
	"value": "15"
},
{
	"type": "Key",
	"comment": "Removes one onion layer.",
	"key": "onion_layer_decrease_key",
	"value": "-"
},
{
	"type": "Key",
	"comment": "Adds one onion layer.",
	"key": "onion_layer_increase_key",
	"value": "+"
},
{
	"type": "Float",
	"comment": "Opacity of the individual onion layers. If this is set high, one can see the other layers pretty well, if one sets it low, a better separation is achieved.",
	"key": "onion_mode_opacity",
	"value": "0.8"
},
{
	"type": "Integer",
	"comment": "",
	"key": "page_number_position_top",
	"value": "15"
},
{
	"type": "String",
	"comment": "The name is used for the settings directory. If this is changed, all previously made notebooks are still on the disk, but cannot be accessed through the program any more.",
	"key": "program_name",
	"value": "jscribble"
},
{
	"type": "Key",
	"comment": "",
	"key": "ruling_graph_toggle_key",
	"value": "g"
},
{
	"type": "Color",
	"comment": "Color of the ruling. It is drawn with the same opaqueness as the onion layers.",
	"key": "ruling_line_color",
	"value": "646464"
},
{
	"type": "Integer",
	"comment": "Spacing between the lines or squares.",
	"key": "ruling_line_spacing",
	"value": "40"
},
{
	"type": "Key",
	"comment": "",
	"key": "ruling_toggle_key",
	"value": "r"
},
{
	"type": "Color",
	"comment": "",
	"key": "scroll_panel_color",
	"value": "64000000"
},
{
	"type": "Integer",
	"comment": "",
	"key": "scroll_panel_padding",
	"value": "5"
},
{
	"type": "Integer",
	"comment": "",
	"key": "scroll_panel_width",
	"value": "20"
},
{
	"type": "Boolean",
	"comment": "Enables panels at the side of the screen to navigate without a keyboard.  Useful for tablet computers.",
	"key": "scroll_panels_show",
	"value": "false"
},
{
	"type": "String",
	"comment": "It does not make much sense to overwrite this since this property is always looked up in the default config.",
	"key": "user_config_filename",
	"value": "config.properties"
}
]
