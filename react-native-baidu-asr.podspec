# react-native-baidu-asr.podspec

require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-baidu-asr"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-baidu-asr
                   DESC
  s.homepage     = "https://github.com/gdoudeng/react-native-baidu-asr"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "DengWeibin" => "dengweibin@beemango.cn" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/gdoudeng/react-native-baidu-asr.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,cc,cpp,m,mm,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."
end

