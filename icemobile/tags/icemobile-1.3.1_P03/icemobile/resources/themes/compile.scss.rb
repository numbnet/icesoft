base_dir = File.expand_path("..",File.dirname(__FILE__))
require File.join(base_dir, 'lib/sass-3.2.5/lib/sass')
require File.join(base_dir, 'lib/sass-3.2.5/lib/sass/exec')

build_dir = File.join(base_dir, 'build/themes')
src_dir = File.join(base_dir, 'themes/**/[^_]*.scss')

files = Dir.glob(src_dir)
files.each do 
    | scss |
        theme = File.dirname(scss).split(File::SEPARATOR).last
        css = File.basename(scss, ".*") + ".css"
        css_path = File.join(build_dir, theme, css);
        if !File.exists?(css_path) or File.mtime(scss) > File.mtime(css_path)
            puts "     [sass compiler] " + scss + " -> " + File.join(build_dir, theme, css)
            #debug opts = Sass::Exec::Sass.new(["--load-path", File.dirname(scss), scss, File.join(build_dir, theme, css), "--debug-info"])
            opts = Sass::Exec::Sass.new(["--load-path", File.dirname(scss), scss, File.join(build_dir, theme, css)])
            opts.parse
        end
end