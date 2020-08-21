package com.github.lolkilee.liverymanager;

public class Presets {

    //"%" = variable based on input

    //Lines to be appended to aircraft.cfg when new livery is added (a320 neo)
    public static String[] aircraftCFGLines = {

            "\n",
            ";=============================%livery_name% livery (start)==================================",
            "[FLTSIM.%n%]",
            "title = \"Airbus a320 Neo %livery_name%\"",
            "model = \"\"",
            "panel = \"\"",
            "sound = \"\"",
            "texture = \"%texture_folder%\"",
            "kb_checklists = \"Boeing747-400_check\"",
            "kb_reference = \"Boeing747-400_ref\"",
            "description = \"TT:AIRCRAFT.DESCRIPTION\"",
            "wip_indicator = 0",
            "ui_manufacturer = \"TT:AIRCRAFT.UI_MANUFACTURER\"",
            "ui_type = \"%airline_name%\"",
            "ui_variation = \"%livery_name%\"",
            "ui_createdby = \"Asobo Studio\"",
            "ui_thumbnailfile = \"\"",
            "ui_certified_ceiling = 39600",
            "ui_max_range = 3500",
            "ui_autonomy = 7",
            "ui_fuel_burn_rate = 5300",
            "atc_id = \"%plane_registration%\"",
            "atc_id_enable = 0",
            "atc_airline = \"\"",
            "atc_flight_number = 1123",
            "atc_heavy = 1",
            "atc_parking_types = \"GATE,RAMP,CARGO\"",
            "atc_parking_codes = \"\"",
            "atc_id_color = \"\"",
            "atc_id_font = \"\"",
            "isAirTraffic = %is_airtraffic%",
            "isUserSlectable = 1",
            "icao_airline = \"%icao_airline_id%\"", //this line is not appended when people select no on prompt
            ";=============================%livery_name% livery (end)====================================",
            "\n"
    };


}
