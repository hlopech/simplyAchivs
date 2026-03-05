package com.example.simplyachivs.domain.model.asceticism

data class AsceticismPreset(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val suggestedDays: Int,
    val category: String,
)

val ASCETICISM_PRESETS = listOf(
    AsceticismPreset("cold_shower",  "Холодный душ",          "Принимайте холодный душ каждый день без исключений",         "🌊", 21,  "Тело"),
    AsceticismPreset("no_social",    "Без соцсетей",          "Полный отказ от Instagram, TikTok и других соцсетей",        "📵",  7,  "Разум"),
    AsceticismPreset("no_sugar",     "Без сахара",            "Исключите сладкое и сахар из рациона полностью",             "🍬", 14,  "Питание"),
    AsceticismPreset("reading",      "Чтение",                "Читайте минимум 30 минут каждый день",                       "📚", 30,  "Разум"),
    AsceticismPreset("running",      "Ежедневная пробежка",   "Пробегайте минимум 20 минут каждый день",                    "🏃", 21,  "Тело"),
    AsceticismPreset("meditation",   "Медитация",             "Медитируйте каждое утро минимум 10 минут",                   "🧘", 30,  "Разум"),
    AsceticismPreset("early_rise",   "Ранний подъём",         "Вставайте до 6:00 утра каждый день",                        "🌅", 14,  "Дисциплина"),
    AsceticismPreset("water",        "2 литра воды",          "Выпивайте не менее 2 литров чистой воды в день",             "💧", 30,  "Тело"),
    AsceticismPreset("no_phone",     "Без телефона перед сном","Убирайте телефон минимум за час до сна",                    "📱", 21,  "Дисциплина"),
    AsceticismPreset("gratitude",    "Дневник благодарности", "Записывайте 3 вещи, за которые благодарны каждый день",      "🙏", 66,  "Разум"),
    AsceticismPreset("no_alcohol",   "Без алкоголя",          "Полный отказ от алкоголя",                                   "🚫", 30,  "Питание"),
    AsceticismPreset("plank",        "Планка каждый день",    "Удерживайте планку минимум 1 минуту каждый день",            "💪", 30,  "Тело"),
)
